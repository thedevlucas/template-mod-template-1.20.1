#version 150

/*
// Based on https://www.shadertoy.com/view/llSGRG by bloodnok and Harha

const float pi = 3.1415927;

float sdSphere( vec3 p, float s )
{
  return length(p)-s;
}

float sdCappedCylinder( vec3 p, vec2 h )
{
  vec2 d = abs(vec2(length(p.xz),p.y)) - h;
  return min(max(d.x,d.y),0.0) + length(max(d,0.0));
}

float sdTorus( vec3 p, vec2 t )
{
  vec2 q = vec2(length(p.xz)-t.x,p.y);
  return length(q)-t.y;
}

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
	vec2 pp = fragCoord.xy/iResolution.xy;
	pp = -1.0 + 2.0*pp;
	pp.x *= iResolution.x/iResolution.y;

    vec3 CameraPosition = vec3(1., 1.620, -5.); //camera position

    vec3 lookAt = CameraPosition + vec3(0.0, 0.0, 1.0);
	vec3 ro = CameraPosition;


	vec3 front = normalize(lookAt - ro);
	vec3 left = normalize(cross(normalize(vec3(0.0,1,-0.1)), front));
	vec3 up = normalize(cross(front, left));
	vec3 rd = normalize(front*1.5 + -left*pp.x + up*pp.y); // rect vector


    vec3 bh = vec3(0.0,0.0,0.0);
    float bhr = 0.1;
    float bhmass = 5.0;
   	bhmass *= 0.001; // premul G

    vec3 p = ro;
    vec3 pv = rd;
    float dt = 0.02;

    vec3 col = vec3(0.0);

    float noncaptured = 1.0;

    vec3 c1 = vec3(0.5,0.46,0.4);
    vec3 c2 = vec3(1.0,0.8,0.6);


    for(float t=length(ro);t<length(ro) + 1.6;t+=0.005)
    {
        p += pv * dt * noncaptured;
        if(distance(ro, p) > 10.) break;

        // gravity
        vec3 bhv = bh - p;
        float r = dot(bhv,bhv);
        pv += normalize(bhv) * ((bhmass) / r);

        noncaptured = smoothstep(0.0, 0.666, sdSphere(p-bh,bhr));

        // Texture for the accretion disc
        float dr = length(bhv.xz);
        float da = atan(bhv.x,bhv.z);
        vec2 ra = vec2(dr,da * (0.01 + (dr - bhr)*0.002) + 2.0 * pi + iTime*0.005 );
        ra *= vec2(10.0,20.0);

        vec3 dcol = mix(c2,c1,pow(length(bhv)-bhr,2.0)) * max(0.0,texture(iChannel1,ra*vec2(0.1,0.5)).r+0.05) * (4.0 / ((0.001+(length(bhv) - bhr)*50.0) ));

        col += max(vec3(0.0),dcol * smoothstep(0.0, 1.0, -sdTorus( (p * vec3(1.0,25.0,1.0)) - bh, vec2(0.8,0.99))) * noncaptured);

        //col += dcol * (1.0/dr) * noncaptured * 0.001;

        // Glow
        col += vec3(1.0,0.9,0.85) * (1.0/vec3(dot(bhv,bhv))) * 0.0033 * noncaptured;

    }

    // BG
    //col += pow(texture(iChannel0, pv).rgb,vec3(3.0)) * noncaptured;

    // FInal color
    fragColor = vec4(col,1.0);
}

*/

uniform sampler2D NoiseSampler;
uniform sampler2D DepthSampler;

in vec2 texCoord;
in vec2 oneTexel;
in vec4 vPosition;

uniform vec2 OutSize;
uniform float STime;
uniform float BreakingProgress;

uniform vec3 CameraPosition;
uniform vec3 CameraFront;
uniform vec3 CameraLeft;
uniform vec3 CameraUp;
uniform float BlackHoleScale;

out vec4 fragColor;

const float pi = 3.1415927;

// The magic matrix to get world coordinates from pixel ones
uniform mat4 InverseTransformMatrix;
// The size of the viewport (typically, [0,0,1080,720])
uniform ivec4 ViewPort;

// From Dave (https://www.shadertoy.com/view/XlfGWN)
float hash13(vec3 p) {
    p  = fract(p * vec3(.16532, .17369, .15787));
    p += dot(p.xyz, p.yzx + 19.19);
    return fract(p.x * p.y * p.z);
}
float hash(float x){ return fract(cos(x*124.123)*412.0); }
float hash(vec2 x){ return fract(cos(dot(x.xy, vec2(2.31, 53.21))*124.123)*412.0); }
float hash(vec3 x){ return fract(cos(dot(x.xyz, vec3(2.31, 53.21, 17.7))*124.123)*412.0); }


float sdSphere(vec3 p, float s) {
    return length(p)-s;
}

float sdCappedCylinder(vec3 p, vec2 h) {
    vec2 d = abs(vec2(length(p.xz), p.y)) - h;
    return min(max(d.x, d.y), 0.0) + length(max(d, 0.0));
}

float sdTorus(vec3 p, vec2 t) {
    vec2 q = vec2(length(p.xz)-t.x, p.y);
    return length(q)-t.y;
}

vec4 CalcEyeFromWindow(in float depth) {
    // derived from https://www.khronos.org/opengl/wiki/Compute_eye_space_from_window_space
    // ndc = Normalized Device Coordinates
    vec3 ndcPos;
    ndcPos.xy = ((2.0 * gl_FragCoord.xy) - (2.0 * ViewPort.xy)) / (ViewPort.zw) - 1;
    ndcPos.z = (2.0 * depth - gl_DepthRange.near - gl_DepthRange.far) / (gl_DepthRange.far - gl_DepthRange.near);
    vec4 clipPos = vec4(ndcPos, 1.);
    vec4 homogeneous = InverseTransformMatrix * clipPos;
    vec4 eyePos = vec4(homogeneous.xyz / homogeneous.w, homogeneous.w);
    return eyePos;
}

void main() {
    //------------------------------------------------------------------------------------------------------------------
    // "It's black holing time" he said as he black holed all over the place
    //------------------------------------------------------------------------------------------------------------------
    vec3 ndc = vPosition.xyz / vPosition.w;//perspective divide/normalize
    vec2 viewportCoord = ndc.xy * 0.5 + 0.5;//ndc is -1 to 1 in GL. scale for 0 to 1

    float sceneDepth = texture(DepthSampler, viewportCoord).x;
    vec3 pixelPosition = CalcEyeFromWindow(sceneDepth).xyz;

    vec2 pp = gl_FragCoord.xy/OutSize.xy;
    pp = -1.0 + 2.0*pp;

    vec3 rayOrigin = CameraPosition / BlackHoleScale;


    vec3 front = CameraFront;
    vec3 left = -CameraLeft;
    vec3 up = CameraUp;
    vec3 rd = normalize(front + left*pp.x + up*pp.y);// rect vector


    vec3 bh = vec3(0.0, 0.0, 0.0);
    float bhr = 0.2;
    float bhmass = 5.0;
    bhmass *= 0.001;// premul G

    vec3 rayPosition = rayOrigin;
    vec3 pv = rd;
    float dt = 0.02;

    rayPosition += pv * hash13(rd + vec3(STime)) * 0.02;

    vec3 col = vec3(0.0);

    float noncaptured = 1.0;

    vec3 c1 = vec3(0.5, 0.5,0.6);
    vec3 c2 = vec3(0.8,0.9,1.0);
    vec3 cGlow = vec3(0.85,0.9,1.0);
    if (BreakingProgress > 0) {
        c1 = vec3(1.0,0.2,0.2);
        c2 = vec3(1.0,0.3,0.2);
        cGlow = vec3(1.0,0.9,0.9);
    }

    vec3 viewDirection = normalize(pixelPosition);
    float obstacleDistance = length(pixelPosition / BlackHoleScale);

    for (float t=length(rayOrigin);t<length(rayOrigin) + 1.5;t+=0.005) {
        rayPosition += pv * dt * noncaptured;

        vec3 rayPositionRelativeToCamera = rayPosition - rayOrigin;
        // Crude approximation : assumes that the obstacle closest to the camera is an infinite plane
        if (dot(rayPositionRelativeToCamera, viewDirection) > obstacleDistance) {
            // stop raycasting if anything is in front of the ray
            break;
        }

        // gravity
        vec3 bhv = bh - rayPosition;
        float r = dot(bhv, bhv);
        pv += normalize(bhv) * ((bhmass) / r);

        noncaptured = smoothstep(0.0, 0.4, sdSphere(rayPosition-bh, bhr));


        // texture the disc
        // need polar coordinates of xz plane
        float dr = length(bhv.xz);
        float da = atan(bhv.x, bhv.z);
        vec2 ra = vec2(dr, da * (0.01 + (dr - bhr)*0.002) + 2.0 * pi + STime*0.005);
        ra *= vec2(10.0, 20.0);

        vec3 dcol = mix(c2, c1, pow(length(bhv)-bhr, 2.0)) * max(0.0, texture(NoiseSampler, ra*vec2(0.1, 0.5)).r+0.05) * (4.0 / ((0.001+(length(bhv) - bhr)*50.0)));

        col += max(vec3(0.0), dcol * smoothstep(0.0, 0.5, -sdTorus((rayPosition * vec3(1.0, 25.0, 1.0)) - bh, vec2(0.8, 0.99))) * noncaptured);

        // quazar, for breaking sequence
        col += dcol * (1.0/dr) * noncaptured * BreakingProgress;

        // glow
        col += cGlow * (0.8/vec3(dot(bhv, bhv))) * 0.0033 * noncaptured;
    }

    col += vec3(0) * noncaptured;

    fragColor = vec4(col, 1.0 - noncaptured);
}


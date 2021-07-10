#version 430 compatibility

precision highp float;

layout(location = 2) uniform sampler2D u_Sampler;

smooth in vec2 position;
smooth in vec4 vertexColor;
smooth in vec2 texCoord;
smooth in vec2 oneTexel;


out vec4 fragColor;

void main() {
    vec4 center = texture(u_Sampler, texCoord);
    vec4 up     = texture(u_Sampler, texCoord + vec2(        0.0, -oneTexel.y));
    vec4 down   = texture(u_Sampler, texCoord + vec2( oneTexel.x,         0.0));
    vec4 left   = texture(u_Sampler, texCoord + vec2(-oneTexel.x,         0.0));
    vec4 right  = texture(u_Sampler, texCoord + vec2(        0.0,  oneTexel.y));
    vec4 uDiff = center - up;
    vec4 dDiff = center - down;
    vec4 lDiff = center - left;
    vec4 rDiff = center - right;
    vec4 sum = uDiff + dDiff + lDiff + rDiff;
    vec3 clamped = clamp(center.rgb - sum.rgb, 0.0, 1.0);
    fragColor = vec4(1.0, 1.0, 1.0, texture(u_Sampler, texCoord).r) * vertexColor;
}
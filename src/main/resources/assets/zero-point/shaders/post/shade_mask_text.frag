#version 430 compatibility

precision highp float;

layout(location = 2) uniform sampler2D Sampler0;

smooth in vec2 position;
smooth in vec4 VertexColor;
smooth in vec2 TexCoord;
smooth in vec2 oneTexel;


out vec4 fragColor;

void main() {
    vec4 center = texture(Sampler0, TexCoord);
    vec4 up     = texture(Sampler0, TexCoord + vec2(0.0, -oneTexel.y));
    vec4 down   = texture(Sampler0, TexCoord + vec2(oneTexel.x, 0.0));
    vec4 left   = texture(Sampler0, TexCoord + vec2(-oneTexel.x, 0.0));
    vec4 right  = texture(Sampler0, TexCoord + vec2(0.0, oneTexel.y));
    vec4 uDiff = center - up;
    vec4 dDiff = center - down;
    vec4 lDiff = center - left;
    vec4 rDiff = center - right;
    vec4 sum = uDiff + dDiff + lDiff + rDiff;
    vec3 clamped = clamp(center.rgb - sum.rgb, 0.0, 1.0);
    fragColor = vec4(1.0, 1.0, 1.0, texture(Sampler0, TexCoord).r) * VertexColor;
}
#version 430 compatibility

precision highp float;

layout(location = 2) uniform sampler2D u_Sampler;

smooth in vec2 position;
smooth in vec4 vertexColor;
smooth in vec2 text;

out vec4 fragColor;

void main() {
    vec2 oneTexel = vec2(1.0/2048.0,1.0/2048.0);
    vec4 center = texture(u_Sampler, text);
    vec4 left   = texture(u_Sampler, text - vec2(oneTexel.x, 0.0));
    vec4 right  = texture(u_Sampler, text + vec2(oneTexel.x, 0.0));
    vec4 up     = texture(u_Sampler, text - vec2(0.0, oneTexel.y));
    vec4 down   = texture(u_Sampler, text + vec2(0.0, oneTexel.y));
    vec4 leftDiff  = center - left;
    vec4 rightDiff = center - right;
    vec4 upDiff    = center - up;
    vec4 downDiff  = center - down;
    vec4 total = clamp(leftDiff + rightDiff + upDiff + downDiff, 0.0, 1.0);
    fragColor = vec4(1.0, 1.0, 1.0 , center.r) * vertexColor * vec4(total.rgb, 1.0) *vec4(1,0,0,1) ;
}
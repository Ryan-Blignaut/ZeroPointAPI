#version 430 compatibility

precision highp float;

layout(location = 2) uniform sampler2D u_Sampler;

smooth in vec2 position;
smooth in vec4 vertexColor;
smooth in vec2 text;

out vec4 fragColor;

void main() {
    fragColor = vec4(1.0, 1.0, 1.0, texture(u_Sampler, text).r) * vertexColor;
}
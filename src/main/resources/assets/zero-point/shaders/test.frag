#version 430 compatibility

precision highp float;

smooth in vec2 position;
smooth in vec4 vertexColor;
smooth in vec2 text;
layout(location = 2) uniform sampler2D u_Sampler;


out vec4 fragColor;

void main() {


    fragColor = texture(u_Sampler, text) * vec4(0.0, 0.5, 0.5, 1.0);
}
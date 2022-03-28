#version 450 core

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
	fragColor = texture(Sampler1, texCoord).r;
}
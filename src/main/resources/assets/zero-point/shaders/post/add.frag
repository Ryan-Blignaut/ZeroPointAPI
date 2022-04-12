#version 430 compatibility

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D Sampler0, Sampler1;

out vec4 fragColor;

void main() {

	fragColor = texture(Sampler0, texCoord) + texture(Sampler1, texCoord);
}

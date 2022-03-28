#version 430 compatibility

precision highp float;

uniform sampler2D Sampler0;

smooth in vec2 position;
smooth in vec4 vertexColor;
smooth in vec2 textureCoord;

out vec4 fragColor;

void main() {

	if (vertexColor.a == 0.0) {
		discard;
	}



	fragColor = texture(Sampler0, textureCoord) *  vertexColor;
}
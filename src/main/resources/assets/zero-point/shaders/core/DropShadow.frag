#version 430 compatibility

precision highp float;

uniform sampler2D Sampler0;
uniform vec2 direction;

smooth in vec2 position;
smooth in vec4 vertexColor;
smooth in vec2 textureCoord;

out vec4 fragColor;

void main() {

	vec4 color = vec4(0.0);
	color += texture(Sampler0, textureCoord - 4.0 * direction) * 0.0162162162;
	color += texture(Sampler0, textureCoord - 3.0 * direction) * 0.0540540541;
	color += texture(Sampler0, textureCoord - 2.0 * direction) * 0.1216216216;
	color += texture(Sampler0, textureCoord - direction) * 0.1945945946;
	color += texture(Sampler0, textureCoord) * 0.2270270270;
	color += texture(Sampler0, textureCoord + direction) * 0.1945945946;
	color += texture(Sampler0, textureCoord + 2.0 * direction) * 0.1216216216;
	color += texture(Sampler0, textureCoord + 3.0 * direction) * 0.0540540541;
	color += texture(Sampler0, textureCoord + 4.0 * direction) * 0.0162162162;
	color = mask * vec4(100.0, 100.0, 100.0, color[3]) / 100.0;

	fragColor = color;


}
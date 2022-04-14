#version 430 compatibility

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

out vec4 fragColor;

void main() {

	vec4 col = texture(Sampler0, texCoord);
	if (texture(Sampler1, texCoord).a <= 0.1) discard;
	fragColor =  col;

/*	vec4 col = texture(Sampler0, texCoord);
	float maskAlpha = texture(Sampler1, texCoord).a;
	if (maskAlpha <= 0.1) col.a = maskAlpha;
	fragColor =  col;*/
}
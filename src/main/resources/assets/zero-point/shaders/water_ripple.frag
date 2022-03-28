#version 430 compatibility


in vec2 position;
in vec2 textureCoord;
in vec4 vertexColor;

in vec4 textureCoordRipple;
uniform float Time;
uniform float Strength;

out vec4 fragColor;
uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;

void main() {
	vec2 textureCoord2 = textureCoord;

	float mask = texture(Sampler1, textureCoord).r;

	vec3 n1 = texture(Sampler2, textureCoordRipple.xy).xyz * 2 - 1;
	vec3 n2 = texture(Sampler2, textureCoordRipple.zw).xyz * 2 - 1;
	vec3 normal = normalize(vec3(n1.xy + n2.xy, n1.z));
	textureCoord2 += normal.xy * Strength * Strength * mask;

	fragColor = vertexColor * texture(Sampler0, textureCoord2);

}
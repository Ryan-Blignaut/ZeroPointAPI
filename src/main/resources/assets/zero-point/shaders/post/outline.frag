#version 430 compatibility

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D Sampler0;

uniform float Radius;
uniform vec2 Direction;
uniform vec4 Colour;

out vec4 fragColor;

#define offset Direction * oneTexel

void main() {



	float centerAlpha = texture(Sampler0, texCoord).a;
	float innerAlpha = centerAlpha;
	for (float r = 1.0; r <= Radius; r++) {
		float alphaCurrent1 = texture(Sampler0, texCoord + offset * r).a;
		float alphaCurrent2 = texture(Sampler0, texCoord - offset * r).a;
		innerAlpha += alphaCurrent1 + alphaCurrent2;
	}

	fragColor = vec4(Colour.rgb, innerAlpha) * step(0.0, -centerAlpha);

}


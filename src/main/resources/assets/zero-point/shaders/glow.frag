#version 430 compatibility

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D Sampler0;

uniform vec2 Direction;
uniform float Radius;
uniform vec4 Colour;
uniform float Exposure;
uniform float Weights[256];

out vec4 fragColor;

#define offset Direction * oneTexel

void main() {

	float innerAlpha = texture(Sampler0, texCoord).a * Weights[0];
	for (float r = 1.0; r <= Radius; r ++) {
		innerAlpha += texture(Sampler0, texCoord + offset * r).a * Weights[int(r)];
		innerAlpha += texture(Sampler0, texCoord - offset * r).a * Weights[int(r)];
	}

	fragColor = vec4(Colour.rgb, mix(innerAlpha, 1.0 - exp(-innerAlpha * Exposure), step(0.0, Direction.y)));
}


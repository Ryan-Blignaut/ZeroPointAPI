#version 430 compatibility

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D Sampler0, Sampler1;
uniform vec2 Direction;
uniform float Radius;
uniform float Weights[256];

#define offset oneTexel * Direction

out vec4 fragColor;

void main() {

	if (Direction.y > 0 && texture(Sampler1, texCoord).a != 0.0) discard;

	float blr = texture(Sampler0, texCoord).a * Weights[0];

	for (float f = 1.0; f <= Radius; f++) {
		blr += texture(Sampler0, texCoord + f * offset).a * (Weights[int(abs(f))]);
		blr += texture(Sampler0, texCoord - f * offset).a * (Weights[int(abs(f))]);
	}

	fragColor = vec4(0.0, 0.0, 0.0, blr);
}

#version 450 core

uniform sampler2D Sampler0;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 BlurDir;
uniform float Radius;

out vec4 fragColor;


void main() {

	vec4 blurred = vec4(0.0);
	float totalStrength = 0.0;
	float totalAlpha = 0.0;
	float totalSamples = 0.0;
	float progRadius = floor(Radius);
	vec4 c = texture(Sampler0, texCoord);
	for (float r = -progRadius; r <= progRadius; r += 1.0) {
		vec4 sample1 = texture(Sampler0, texCoord + oneTexel * r * BlurDir);

		// Accumulate average alpha
		totalAlpha = totalAlpha + sample1.a;
		totalSamples = totalSamples + 1.0;

		// Accumulate smoothed blur
		float strength = 1.0 - abs(r / progRadius);
		totalStrength = totalStrength + strength;
		blurred = blurred + sample1;
	}

	//	totalAlpha = totalAlpha/( progRadius * 2.0 + 1.0);
	fragColor = vec4(blurred.rgb / (progRadius * 2.0 + 1.0), c.a);
}



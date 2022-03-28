#version 430 compatibility

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D Sampler0;
uniform vec2 BlurDir;
uniform float Radius;

out vec4 fragColor;

void main() {
	vec4 blurred = vec4(0.0);
	float totalStrength = 0.0;
	float totalAlpha = 0.0;
	float totalSamples = 0.0;
	for (float r = -Radius; r <= Radius; r += 1.0) {
		vec4 sample1 = texture(Sampler0, texCoord + oneTexel * r * BlurDir);
		// Accumulate average alpha
		totalAlpha = totalAlpha + sample1.a;
		totalSamples = totalSamples + 1.0;

		// Accumulate smoothed blur
//		float strength = (2.0 - abs(r / Radius))*sample.a;
//		float strength = sample1.a;
//		float strength = 1.0 - abs(r);
//		totalStrength = totalStrength + strength;
//		blurred = blurred + sample1;

		float strength = 1.0 - abs(r / Radius);
		totalStrength = totalStrength + strength;
		blurred = blurred + sample1;


	}
//	float alpha = totalAlpha/totalSamples;
	fragColor = vec4(blurred.rgb / /*totalStrength*/(Radius*2.0 + 1.0), totalAlpha);
}
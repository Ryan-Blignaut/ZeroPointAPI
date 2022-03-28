#version 430 compatibility

in vec2 position;
in vec2 textureCoord;
in vec4 vertexColor;

uniform vec2 TexelSize;
uniform float Radius;
uniform float Divider;
uniform float MaxSample;
uniform float MixFactor;
uniform float MinAlpha;
uniform vec2 Dimensions;
uniform int Blur;

/*uniform sampler2D image;
uniform float imageMix;
uniform bool useImage;*/


out vec4 fragColor;
uniform sampler2D Sampler0;

vec4 blur13(sampler2D image, vec2 uv, vec2 resolution, vec2 direction) {
	vec4 color = vec4(0.0);
	vec2 off1 = vec2(1.411764705882353) * direction;
	vec2 off2 = vec2(3.2941176470588234) * direction;
	vec2 off3 = vec2(5.176470588235294) * direction;
	if (texture2D(image, uv + (off1 / resolution)).a == 0
	|| texture2D(image, uv + (off2 / resolution)).a == 0
	|| texture2D(image, uv + (off3 / resolution)).a == 0) {
		return texture(image, uv);
	}
	color += texture(image, uv) * 0.1964825501511404;
	color += texture(image, uv + (off1 / resolution)) * 0.2969069646728344;
	color += texture(image, uv - (off1 / resolution)) * 0.2969069646728344;
	color += texture(image, uv + (off2 / resolution)) * 0.09447039785044732;
	color += texture(image, uv - (off2 / resolution)) * 0.09447039785044732;
	color += texture(image, uv + (off3 / resolution)) * 0.010381362401148057;
	color += texture(image, uv - (off3 / resolution)) * 0.010381362401148057;
	return color;
}

void main() {
	vec4 centerCol = texture(Sampler0, textureCoord);

	if (Blur == 1) {
		if (centerCol.a != 0) {
			centerCol = blur13(Sampler0, textureCoord, Dimensions, vec2(2, 2));
		}
	}

	float alpha = 0;

	if (centerCol.a != 0) {
		fragColor = vec4(mix(centerCol.rgb, vertexColor.rgb, MixFactor), centerCol.a);
	} else {
		for (float x = -Radius; x < Radius; x++) {
			for (float y = -Radius; y < Radius; y++) {
				vec4 currentColor = texture(Sampler0, textureCoord + vec2(TexelSize.x * x, TexelSize.y * y));

				if (currentColor.a != 0)
				alpha += Divider > 0 ? max(0, (MaxSample - distance(vec2(x, y), vec2(0))) / Divider) : 1;
				alpha *= MinAlpha;
			}
		}
		fragColor = vec4(vertexColor.rgb, alpha);
	}

}
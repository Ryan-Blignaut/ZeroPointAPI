#version 430 compatibility

in vec2 position;
in vec2 textureCoord;
in vec4 vertexColor;

uniform vec2 u_model_size;
uniform float u_threshold;
uniform float u_width;
uniform float u_step_start;
uniform float u_step_end;
uniform vec4 u_color;
uniform vec4 u_far_color;
uniform vec4 u_low_color;
uniform float u_low_color_fade;
uniform float u_mesh_pad;

out vec4 fragColor;
uniform sampler2D Sampler0;


float get_step_alpha(float d, float s, float e)
{
	return smoothstep(s, s + 1.0 - e, d) * (1.0 - smoothstep(e, 1.0, d));
}
bool find_opaque(float x, float y, vec2 pos, vec2 pxo, float threshold) {
	// x,y
	if (texture(Sampler0, clamp(pos + pxo, 0.0, 1.0)).a >= threshold) {
		return true;
	}
	// -x, y (if x is not 0)
	if (x > 0.1) {
		if (texture(Sampler0, clamp(pos + vec2(-pxo.x, pxo.y), 0.0, 1.0)).a >= threshold) {
			return true;
		}
	}
	// x, -y (if y is not 0)
	if (y > 0.1) {
		if (texture(Sampler0, clamp(pos + vec2(pxo.x, -pxo.y), 0.0, 1.0)).a >= threshold) {
			return true;
		}
		// -x, -y (if both x and y not 0)
		if (x > 0.1) {
			if (texture(Sampler0, clamp(pos - pxo, 0.0, 1.0)).a >= threshold) {
				return true;
			}
		}
	}
	return false;
}
float opaque_distance(vec2 pos, float threshold, float u_width, vec2 pixel_size, float max_dist) {
	float nearest = 0.0;
	for (float x = 1.0; x <= u_width; x += 1.0) {
		float x_dist = pow(x, 2);
		for (float y = 0.0; y <= x; y += 1.0) {
			float d = pow(y, 2) + x_dist;
			if (nearest > 0.1) max_dist = nearest;
			if (d > max_dist) break;
			vec2 pxo = vec2(x, y) * pixel_size;
			// best of the four pixels (-x, y), (x, y), (-x, -y), (x, -y)
			if (find_opaque(x, y, pos, pxo, threshold)) {
				nearest = d;
			}
			if (x > y) {
				vec2 pyo = vec2(y, x) * pixel_size;
				// best of the four pixels (-y, x), (y, x), (-y, -x), (y, -x)
				if (find_opaque(y, x, pos, pyo, threshold)) {
					nearest = d;
				}
			}
		}
	}
	return nearest;
}

void main() {

	//	fragColor = texture(Sampler0, textureCoord);
	vec4 col = texture(Sampler0, textureCoord);
	vec2 padded_size = u_model_size;
	if (u_mesh_pad > 0.5) padded_size += vec2(u_width) * 2;
	vec2 pos = textureCoord;

	vec2 pixel_size = (vec2(1.) / padded_size);

	float max_dist = pow(u_width, 2);

	// only want outlines where the image is part or fully transparent
	if (col.a < 0.98) {
		// the square distance of nearest threshold alpha pixel

		float near = opaque_distance(pos, u_threshold, u_width, pixel_size, max_dist);

		if (near > 0.1) {
			// Now we can do the sqrt
			float dist = sqrt(near) / u_width;
			float color_dist = (dist - u_step_start) / (u_step_end - u_step_start);
			vec4 color = mix(u_color, u_far_color, color_dist);
			float alpha = get_step_alpha(dist, u_step_start, u_step_end);

			if (u_low_color != u_color && alpha < 0.99) {
				color = (u_low_color_fade > 0.5) ?
				mix(u_low_color, color, clamp(alpha * alpha, 0.0, 1.0)) :
				u_low_color;
			}
			alpha *= u_color.a;
			// this pixel should be altered

			if (col.a > 0.05 && alpha > 0.05) {
				// some edge pixel that has some opacity
				fragColor = vec4(mix(color.rgb, fragColor.rgb, fragColor.a / (fragColor.a + alpha)), 1.0) * (1.0, 1.0, 1.0, max(alpha, fragColor.a));
			} else {
				fragColor = vec4(color * (1.0, 1.0, 1.0, alpha));
			}
		}
	}

}
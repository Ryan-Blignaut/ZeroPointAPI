#version 430 compatibility

precision highp float;

uniform sampler2D Sampler0;

smooth in vec3 position;
smooth in vec4 vertexColor;
smooth in vec2 textureCoord;

uniform float time;
out vec4 fragColor;

vec3 hsl2rgb(vec3 c) {
	vec3 rgb = clamp(abs(mod(c.x*6.0+vec3(0.0, 4.0, 2.0), 6.0)-3.0)-1.0, 0.0, 1.0);
	return c.z + c.y * (rgb-0.5)*(1.0-abs(2.0*c.z-1.0));
}

void main() {
	float a = 1 - texture(Sampler0, textureCoord).r;
	float w = 0.3;
	float e = 0.35;
	float d =  1 - smoothstep(w, w+e, a);

/*	vec2 frag_uv = gl_FragCoord.xy / vec2(2560, 1440);
	vec4 rainbow = vec4(hsl2rgb(vec3((time + frag_uv.x + frag_uv.y), 0.5, 0.5)), 1.0);*/

	fragColor = vertexColor * vec4(1.0, 1.0, 1.0, d) ;
}


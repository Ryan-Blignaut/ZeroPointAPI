#version 430 compatibility

uniform vec2 CenterPosition;
uniform float Time;
uniform vec2 P;

smooth in vec2 position;
smooth in vec4 vertexColor;

out vec4 fragColor;

mat2 rotate2d(float angle){
	return mat2(cos(angle), -sin(angle), sin(angle), cos(angle));
}

void main() {
	vec2 p = (position*2.0-P) / min(P.x, P.y);
	p = rotate2d(Time * 1.0) * p;
	float t = 0.01 / abs(abs(sin(1.0)) - length(p));
	fragColor = vec4(vec3(t) * (vec3(p.x, p.y, 1.0)*vec3(2.0)), 1);
}
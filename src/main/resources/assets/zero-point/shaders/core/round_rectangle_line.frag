#version 430 compatibility

precision highp float;

uniform vec2 Radius;
uniform float Thickness;
uniform vec4 Rectangle;

smooth in vec2 position;
smooth in vec4 vertexColor;

out vec4 fragColor;


float sdRoundBox(vec2 p, vec2 b, vec4 r)
{
	r.xy = (p.x>0.0)?r.xy : r.zw;
	r.x  = (p.y>0.0)?r.x  : r.y;

	vec2 q = abs(p) - b + r.x;
	return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r.x;
}

void main() {
	// r.x = roundness top-right
	// r.y = roundness boottom-right
	// r.z = roundness top-left
	// r.w = roundness bottom-left


		float sdf = length(max(vec2(0.0), max(Rectangle.xy - position, position - Rectangle.zw))) - Radius.x;
		float alpha = 1.0 - smoothstep(-Radius.y, 0.0, abs(sdf) - Thickness);
//	float alpha = 1.0 - smoothstep(-Radius.y, 0.0, abs(sdRoundBox(position,  vec2(.0, .0), vec4(0., 0.0, 0.4, -4.231))) - Thickness);

	if (alpha == 0.0) discard;
	fragColor = vertexColor * vec4(1.0, 1.0, 1.0, alpha);
}
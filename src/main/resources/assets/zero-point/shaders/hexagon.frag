#version 430 compatibility

precision highp float;

uniform vec2 u_Center_Pos;

smooth in vec2 position;
smooth in vec4 vertexColor;

out vec4 fragColor;


void main() {
    if (vertexColor.a == 0.0) {
        discard;
    }
    const float hexSize = .05;
    // 1.7320508 is sqrt(3)
    const vec2 s = vec2(1, 1.7320508);
    vec2 pos = abs(position);

     float alpha = 1.0 - smoothstep(-0.3,.0,max(dot(pos, s*.5), pos.x) - hexSize);

     fragColor = vertexColor * vec4(1.0, 1.0, 1.0, alpha);
}
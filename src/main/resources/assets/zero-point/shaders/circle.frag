#version 430 compatibility

precision highp float;

uniform vec2 u_Center_Pos;
uniform vec2 u_Radius;

smooth in vec2 position;
smooth in vec4 vertexColor;

out vec4 fragColor;


void main() {
    if (vertexColor.a == 0.0) {
        discard;
    }

    //Distance from position to center
    float v = length(position - u_Center_Pos);
    //1 - interpelated value from u_Radius - feather to u_Radius where v is source
    float alpha = 1.0 - smoothstep(u_Radius.x - u_Radius.y, u_Radius.x, v);

    fragColor = vertexColor * vec4(1.0, 1.0, 1.0, alpha);
}
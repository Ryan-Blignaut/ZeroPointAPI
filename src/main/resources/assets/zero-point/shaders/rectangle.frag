#version 430 compatibility

precision highp float;

uniform vec2 u_Radius;

smooth in vec2 position;
smooth in vec4 vertexColor;

out vec4 fragColor;

void main() {

    if (vertexColor.a == 0.0) {
        discard;
    }

    float v =  u_Radius.x;

    float a = 1.0 - smoothstep(-u_Radius.y, 0.0, v);

    fragColor = vec4(1.0, 1.0, 1.0, a) * vertexColor;
}
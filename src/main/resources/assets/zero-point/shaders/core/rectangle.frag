#version 430 compatibility

precision highp float;

uniform vec2 Radius;
uniform vec4 Rectangle;

smooth in vec2 position;
smooth in vec4 vertexColor;

out vec4 fragColor;
float sdBox(in vec2 p, in vec2 b)
{
    vec2 d = abs(p)-b;
    return length(max(d, 0.0)) + min(max(d.x, d.y), 0.0);
}

void main() {

    if (vertexColor.a == 0.0) {
        discard;
    }

    fragColor =  vertexColor ;//* vec4(1.0, 1.0, 1.0, a);

    /*  float v =  u_Radius.x;

      float a = 1.0 - smoothstep(-u_Radius.y, 0.0, v);

      fragColor = vec4(1.0, 1.0, 1.0, a) * vertexColor;*/
}
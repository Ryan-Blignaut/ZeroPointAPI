#version 430 compatibility

precision highp float;

uniform vec2 CenterPosition;
uniform vec2 Radius;

smooth in vec2 position;
smooth in vec4 vertexColor;

out vec4 fragColor;


void main() {
    if (vertexColor.a == 0.0) {
        discard;
    }

    //Distance from position to center
    float dist = length(position - CenterPosition);
    //1 - interpelated value from radius - feather to radius where v is source
    float alpha = abs(smoothstep(radius.x - radius.y, radius.x, dist));//* (1.0 - smoothstep(radius.x - radius.y, radius.x, v));

    fragColor = vertexColor * vec4(1.0, 1.0, 1.0, alpha);
}
#version 450 core

uniform sampler2D Sampler0;
uniform float Radius;
uniform vec2 Direction;

in vec2 TexCoord;
in vec2 OneTexel;

out vec4 fragColor;

void main() {
    fragColor = vec4(1.0, 1.0, 0.6, 1.0);
}
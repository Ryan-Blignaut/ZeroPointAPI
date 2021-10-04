#version 450 core

uniform sampler2D u_Texture;
uniform float u_Radius;
uniform vec2 u_Direction;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
    vec3 finVar = vec3(0.0);
    for (float i = -u_Radius; i <= u_Radius; i += 2.0) {
        finVar += texture(u_Texture, texCoord + oneTexel * (i + 0.5) * u_Direction).rgb;
    }
    fragColor = vec4(finVar / (u_Radius + 1.0), 1.0);
}
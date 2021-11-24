#version 450 core

uniform sampler2D Sampler0;
//uniform float Radius;
//uniform vec2 Direction;
uniform vec2 InSize;
uniform vec2 BlurDir;
uniform float Radius;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {

    vec3 final = vec3(0.0);

    for (float i = -Radius; i <= Radius; i += 2.0) {
        final += texture(Sampler0, texCoord + oneTexel * (i + 0.5) * BlurDir).rgb;
    }

    fragColor = texture(Sampler0, texCoord);//vec4(final / (Radius + 1.0), 1.0);






    /*    vec3 finVar = vec3(0.0);
        for (float i = -3; i <= 3; i += 2.0) {
            finVar += texture(Sampler0, texCoord + oneTexel * (i + 0.5) * vec2(1.0, 0.0)).rgb;
        }
        fragColor = vec4(finVar, 1.0);//texture(Sampler0, texCoord);/*//* vec4(1.0, texCoord, 1.0);//vec4(finVar / (3 + 1.0), 1.0);*/
}
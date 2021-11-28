#version 450 core

uniform sampler2D Sampler0;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main(){

    vec4 center = texture(Sampler0, texCoord);

    vec4 left = texture(Sampler0, texCoord - vec2(oneTexel.x, 0.0));
    vec4 right = texture(Sampler0, texCoord + vec2(oneTexel.x, 0.0));
    vec4 up = texture(Sampler0, texCoord - vec2(0.0, oneTexel.y));
    vec4 down = texture(Sampler0, texCoord + vec2(0.0, oneTexel.y));
    float leftDiff  = left.a - center.a;
    float rightDiff = right.a - center.a;
    float upDiff    = up.a - center.a;
    float downDiff  = down.a - center.a;
    float total = leftDiff + rightDiff + downDiff + upDiff;
    vec3 outColor = center.rgb * center.a + left.rgb * left.a + right.rgb * right.a + up.rgb * up.a + down.rgb * down.a;
    fragColor = vec4(outColor / total, clamp(total, 0.0, 1.0));

}
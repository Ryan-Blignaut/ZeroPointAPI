#version 430 compatibility

precision highp float;

smooth in vec2 position;
smooth in vec4 vertexColor;

const float HALF_PI = 1.57079632679;
const float      PI = 3.14159265359;
const float  TWO_PI = 6.28318530718;

out vec4 fragColor;

float polygon(vec2 pos, float n, float radius)
{
    vec2 p = pos;
    float angle = atan(p.x, p.y) + PI;
    float r = TWO_PI / n;
    float d = cos(floor(0.5 + angle / r) * r - angle) * length(p) / radius;
    return smoothstep(0.41, 0.4, d);
}

void main()
{
    float n = 6.0;
    float intensity = 0.8 * polygon(position, 3.0, 0.4);
    fragColor = mix(vertexColor, vec4(1.0, 0.0, 0.0, 1.0), intensity);//mix(fragColor, vec4(1.0, 0.0, 0.0, 1.0), intensity);
}
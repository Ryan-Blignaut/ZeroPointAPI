#version 430 compatibility
#define ITERATIONS 320
#define GOLDEN_ANGLE 2.39996

precision highp float;

uniform sampler2D u_Texture;
uniform float size;


smooth in vec2 position;
smooth in vec4 vertexColor;
smooth in vec2 text;

out vec4 fragColor;

mat2 rot = mat2(cos(GOLDEN_ANGLE), sin(GOLDEN_ANGLE), -sin(GOLDEN_ANGLE), cos(GOLDEN_ANGLE));

vec3 Bokeh(sampler2D tex, vec2 uv, float radius)
{
    vec3 acc = vec3(0), div = acc;
    float r = 1.;
    vec2 vangle = vec2(0.0, radius*.01 / sqrt(float(ITERATIONS)));

    for (int j = 0; j < ITERATIONS; j++)
    {
        // the approx increase in the scale of sqrt(0, 1, 2, 3...)
        r += 1. / r;
        vangle = rot * vangle;
        vec3 col = texture(tex, uv + (r-1.) * vangle).xyz;/// ... Sample the image
        col = col * col *1.8;// ... Contrast it for better highlights - leave this out elsewhere.
        vec3 bokeh = pow(col, vec3(4));
        acc += col * bokeh;
        div += bokeh;
    }
    return acc / div;
}

void main() {
//    texture(u_Texture, text)
    fragColor =  vec4(Bokeh(u_Texture, text, size), 1.0);
}
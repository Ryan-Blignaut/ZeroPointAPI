#version 430 compatibility

precision highp float;

uniform vec2 u_Radius;
uniform vec4 u_InnerRect;
uniform sampler2D u_Texture;

smooth in vec2 position;
smooth in vec4 vertexColor;
smooth in vec2 text;

out vec4 fragColor;

void main() {

    if (vertexColor.a == 0.0) {
        discard;
    }

    vec2 tl = u_InnerRect.xy - position;
    vec2 br = position - u_InnerRect.zw;

    vec2 dis = max(br, tl);

    float v = length(max(vec2(0.0), dis)) - u_Radius.x;

    float a = 1.0 - smoothstep(-u_Radius.y, 0.0, v);

    fragColor = texture(u_Texture, text) * vec4(1.0, 1.0, 1.0, a);
}
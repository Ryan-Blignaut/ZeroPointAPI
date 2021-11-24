#version 430 compatibility

precision highp float;

uniform vec2 Radius;
uniform vec4 Rectangle;

smooth in vec2 position;
smooth in vec4 vertexColor;

out vec4 fragColor;

void main() {

    if (vertexColor.a == 0.0) {
        discard;
    }


   /* vec2 tl = u_InnerRect.xy - position;
    vec2 br = position - u_InnerRect.zw;

    vec2 dis = max(br, tl);

    float v = length(max(vec2(0.0), dis)) - u_Radius.x;

    float a = 1.0 - smoothstep(-u_Radius.y, 0.0, v);

    fragColor = VertexColor * vec4(1.0, 1.0, 1.0, a);*/

    float sdf = length(max(vec2(0.0),max(Rectangle.xy - position, position - Rectangle.zw))) - Radius.x;
    float alpha = 1.0 - smoothstep(-Radius.y, 0.0, sdf);
    fragColor = vertexColor * vec4(1.0, 1.0, 1.0, alpha);
}
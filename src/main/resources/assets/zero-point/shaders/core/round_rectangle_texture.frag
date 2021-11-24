#version 430 compatibility

precision highp float;

uniform vec2 Radius;
uniform vec4 Rectangle;
uniform sampler2D Sampler0;

smooth in vec2 position;
smooth in vec4 vertexColor;
smooth in vec2 textureCoord;

out vec4 fragColor;

void main() {


    if (vertexColor.a == 0.0) {
        discard;
    }

    float sdf = length(max(Rectangle.xy - position, position - Rectangle.zw)) - Radius.x;
    float alpha = 1.0 - smoothstep(-Radius.y, 0.0, sdf);

    fragColor = texture(Sampler0, textureCoord) * vec4(1.0, 1.0, 1.0, alpha);
}
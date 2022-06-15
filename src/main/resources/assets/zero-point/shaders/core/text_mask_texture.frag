#version 430 compatibility

precision highp float;

uniform sampler2D Sampler0;

smooth in vec2 position;
smooth in vec4 vertexColor;
smooth in vec2 textureCoord;

out vec4 fragColor;

void main() {
    fragColor = vertexColor * vec4(1.0, 1.0, 1.0, texture(Sampler0, textureCoord).r);
    if (fragColor.a < 0.1) discard;
  /*  if (fragColor.a < 0.3) {
        discard;
    }*/
}
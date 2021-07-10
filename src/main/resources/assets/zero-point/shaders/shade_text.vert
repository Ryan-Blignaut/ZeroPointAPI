#version 450 core

in vec3 Position;
in vec4 Color;
in vec2 UV;


uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec2 InSize;

smooth out vec2 position;
smooth out vec4 vertexColor;
smooth out vec2 texCoord;
smooth out vec2 oneTexel;

void main() {


    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    position = Position.xy;
    vertexColor = Color;
    texCoord = UV;
    //one texel is basicaly one pixel
    oneTexel = 1.0 / InSize;
}
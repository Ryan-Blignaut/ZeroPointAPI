#version 450 core

in vec4 Position;
in vec2 UV;
uniform vec2 TextureSize;
uniform mat4 ProjMat;

out vec2 texCoord;
out vec2 oneTexel;

void main() {


    vec4 outPos = ProjMat * vec4(Position.xy, 0.0, 1.0);
    gl_Position = vec4(outPos.xy, 0.2, 1.0);
    //one texel is basicaly one pixel
    oneTexel = 1.0 / TextureSize;
    texCoord = UV;// Position.xy / TextureSize;
}
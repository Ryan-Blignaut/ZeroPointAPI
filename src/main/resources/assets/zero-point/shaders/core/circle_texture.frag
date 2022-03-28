#version 430 compatibility

precision highp float;

uniform vec2 CenterPosition;
uniform vec2 Radius;
uniform sampler2D sampler0;
uniform float T;


smooth in vec2 position;
smooth in vec4 vertexColor;
smooth in vec2 textureCoord;

out vec4 fragColor;


void main() {
    if (vertexColor.a == 0.0) {
        discard;
    }

    //Distance from position to center
    float dist = length(position - CenterPosition);
    //1 - interpelated value from u_Radius - feather to u_Radius where v is source
    float alpha = 1.0 - smoothstep(Radius.x - Radius.y, Radius.x, dist);

    fragColor = vec4(dist);//texture(sampler0, textureCoord) * vertexColor * vec4(1.0, 1.0, 1.0, alpha);
}
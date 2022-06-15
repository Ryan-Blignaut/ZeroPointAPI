#version 430 compatibility

in vec2 position;
in vec2 textureCoord;
in vec4 vertexColor;

uniform float Strength;

uniform sampler2D Sampler0;

uniform float StrokeSize;
uniform vec2 OneTextile;

out vec4 fragColor;

void main() {
	vec4 textureCol = texture(Sampler0, textureCoord);
	fragColor =textureCol;


	/*	if (textureCol != vec4(0.0)) {
			discard;
		}
		vec4 buildCol = vec4(0.0);
		float closest = StrokeSize;

		for (float x = -StrokeSize; x<= StrokeSize; x++)
		for (float y = -StrokeSize; y <= StrokeSize; y++){
			vec4 rCol = texture(Sampler0, textureCoord + x * OneTextile.x + y * OneTextile.y);
			if (rCol != vec4(0.0)){
				float dist = sqrt(x * x + y * y);
				if (dist < closest)
				{
					closest = dist;
					buildCol = rCol;
				}
			}
		}

		buildCol.a = max(0, (StrokeSize - closest - 1)/StrokeSize);
		fragColor = buildCol;*/
}
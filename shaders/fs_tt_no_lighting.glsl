#version 330 core

in vec3 fragPos;
in vec3 ourNormal;
in vec2 ourTexCoord;

out vec4 fragColor;

uniform sampler2D first_texture;

void main() {

  fragColor = texture(first_texture, ourTexCoord) * 0.8;
}


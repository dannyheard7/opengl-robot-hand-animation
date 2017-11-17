#version 330 core

in vec3 fragPos;
in vec3 ourNormal;
in vec2 ourTexCoord;

out vec4 fragColor;

uniform sampler2D first_texture;
uniform vec3 viewPos;

struct DirLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};
uniform DirLight dirLight;

struct PointLight {
  vec3 position;

  vec3 ambient;
  vec3 diffuse;
  vec3 specular;

  float constant;
  float linear;
  float quadratic;
};

#define NR_POINT_LIGHTS 1
uniform PointLight pointLights[NR_POINT_LIGHTS];


struct SpotLight {
  vec3 position;
  vec3 ambient;
  vec3 diffuse;
  vec3 specular;

  float coneAngle;
  vec3 coneDirection;
};

uniform SpotLight spotLight;

struct Material {
  vec3 ambient;
  vec3 diffuse;
  vec3 specular;
  float shininess;
};

uniform Material material;

vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir)
{
     vec3 lightDir = normalize(-light.direction);
     float diff = max(dot(normal, lightDir), 0.0);

     vec3 reflectDir = reflect(-lightDir, normal);
     float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

     vec3 ambient = light.ambient * material.ambient * texture(first_texture, ourTexCoord).rgb;
     vec3 diffuse = light.diffuse * (diff * material.diffuse) * texture(first_texture, ourTexCoord).rgb;
     vec3 specular = light.specular * (spec * material.specular);

     return (ambient + diffuse + specular);
}

vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(light.position - fragPos);
    float diff = max(dot(normal, lightDir), 0.0);

    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

    vec3 ambient = light.ambient * material.ambient * texture(first_texture, ourTexCoord).rgb;
     vec3 diffuse = light.diffuse * (diff * material.diffuse) * texture(first_texture, ourTexCoord).rgb;
     vec3 specular = light.specular * (spec * material.specular);

    float distance    = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    ambient  *= attenuation;
    diffuse   *= attenuation;
    specular *= attenuation;

    return (ambient + diffuse + specular);
}

vec3 CalcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(light.position - fragPos);
     float diff = max(dot(normal, lightDir), 0.0);

     vec3 reflectDir = reflect(-lightDir, normal);
     float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

     vec3 ambient = light.ambient * material.ambient * texture(first_texture, ourTexCoord).rgb;
     vec3 diffuse = light.diffuse * (diff * material.diffuse) * texture(first_texture, ourTexCoord).rgb;
     vec3 specular = light.specular * (spec * material.specular);

     float attenuation = 1.0;
    float lightToSurfaceAngle = degrees(acos(dot(-fragPos, normalize(light.coneDirection))));
    if(lightToSurfaceAngle > light.coneAngle){
        attenuation = 0.0;
    }

    return ambient + attenuation*(diffuse + specular);
}

void main() {
  vec3 norm = normalize(ourNormal);
  vec3 viewDir = normalize(viewPos - fragPos);

  vec3 result = CalcDirLight(dirLight, norm, viewDir);

    for(int i = 0; i < NR_POINT_LIGHTS; i++)
        result += CalcPointLight(pointLights[i], norm, fragPos,  viewDir);

    result += CalcSpotLight(spotLight, norm, fragPos, viewDir);

  fragColor = vec4(result, 1.0);
}


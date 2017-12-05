#version 330 core

in vec3 fragPos;
in vec3 ourNormal;
in vec2 ourTexCoord;

out vec4 fragColor;
 
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

#define NR_POINT_LIGHTS 2
uniform PointLight pointLights[NR_POINT_LIGHTS];


struct SpotLight {
  vec3 position;
  vec3 ambient;
  vec3 diffuse;
  vec3 specular;

  float constant;
  float linear;
  float quadratic;

  vec3  direction;
  float cutOff;
  float outerCutOff;
};

uniform SpotLight spotLight;

struct Material {
  sampler2D diffuse;
  sampler2D specular;
  float shininess;
}; 
  
uniform Material material;

/* Light calculation code is from learnopengl.com */

vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir)
{
     vec3 lightDir = normalize(-light.direction);
     float diff = max(dot(normal, lightDir), 0.0);

     vec3 reflectDir = reflect(-lightDir, normal);
     float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

     vec3 ambient = light.ambient * vec3(texture(material.diffuse, ourTexCoord));
     vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, ourTexCoord));
     vec3 specular = light.specular * spec * vec3(texture(material.specular, ourTexCoord));

     return (ambient + diffuse + specular);
}


vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(light.position - fragPos);
    float diff = max(dot(normal, lightDir), 0.0);

    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

    vec3 ambient = light.ambient * vec3(texture(material.diffuse, ourTexCoord));
    vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, ourTexCoord));
    vec3 specular = light.specular * spec * vec3(texture(material.specular, ourTexCoord));

    float distance    = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    ambient  *= attenuation;
    diffuse   *= attenuation;
    specular *= attenuation;

    return (ambient + diffuse + specular);
}

vec3 CalcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir) {
    vec3 lightDir = normalize(light.position - fragPos);


    float diff = max(dot(normal, lightDir), 0.0);

    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

    // attenuation
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    // spotlight intensity
    float theta = dot(lightDir, normalize(-light.direction));
    float epsilon = light.cutOff - light.outerCutOff;
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);

    // combine results
    vec3 ambient = light.ambient * vec3(texture(material.diffuse, ourTexCoord));
    vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, ourTexCoord));
    vec3 specular = light.specular * spec * vec3(texture(material.specular, ourTexCoord));

    ambient *= attenuation * intensity;
    diffuse *= attenuation * intensity;
    specular *= attenuation * intensity;

    return (ambient + diffuse + specular);
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




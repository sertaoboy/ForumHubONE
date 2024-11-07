CREATE TABLE cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,  -- Ajustado para armazenar o nome da enum Categoria como string
    ativo BOOLEAN NOT NULL           -- Usando BOOLEAN para refletir o tipo Boolean na entidade
);

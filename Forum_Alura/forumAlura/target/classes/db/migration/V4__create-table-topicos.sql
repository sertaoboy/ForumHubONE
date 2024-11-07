CREATE TABLE topicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status BOOLEAN NOT NULL,
    usuario_id BIGINT NOT NULL,  -- Alterado de autor_id para usuario_id
    curso_id BIGINT NOT NULL,
    ativo BOOLEAN NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),  -- Alterado aqui também
    FOREIGN KEY (curso_id) REFERENCES cursos(id)
);

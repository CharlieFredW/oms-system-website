CREATE TYPE orderStatus AS ENUM (
    'PROCESSING',
    'SHIPPED',
    'PACKAGED',
    'FAILED',
    'PROCESSED',
    'DELIVERED'
    );
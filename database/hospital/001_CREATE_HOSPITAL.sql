DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'hospital') THEN
       CREATE DATABASE hospital;
END IF;
END $$;
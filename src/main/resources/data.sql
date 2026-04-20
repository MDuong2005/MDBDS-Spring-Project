INSERT INTO categories (name, slug) VALUES ('Đất nền', 'dat-nen') ON CONFLICT (slug) DO NOTHING;
INSERT INTO categories (name, slug) VALUES ('Nhà ở', 'nha-o') ON CONFLICT (slug) DO NOTHING;
INSERT INTO categories (name, slug) VALUES ('Chung cư', 'chung-cu') ON CONFLICT (slug) DO NOTHING;
INSERT INTO categories (name, slug) VALUES ('Dự án', 'du-an') ON CONFLICT (slug) DO NOTHING;

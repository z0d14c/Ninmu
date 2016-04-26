CREATE TABLE settings ( _id INTEGER PRIMARY KEY AUTOINCREMENT,
                setting_name TEXT NOT NULL,
                setting_value INTEGER NOT NULL);
INSERT INTO settings (setting_name, setting_value) VALUES ("session_length", 20);
INSERT INTO settings (setting_name, setting_value) VALUES ("break_length", 5);
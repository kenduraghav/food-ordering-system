-- Insert sample restaurants
INSERT INTO restaurants (id, name, cuisine_type, address, phone, description, delivery_fee, min_order_amount, average_delivery_time, is_active, created_at, updated_at) VALUES
('rest1', 'Pizza Palace', 'Italian', '123 Main St, Chennai', '+91-9876543210', 'Best pizza in town', 2.99, 15.00, 30, true, NOW(), NOW()),
('rest2', 'Spice Garden', 'Indian', '456 Food St, Chennai', '+91-9876543211', 'Authentic Indian cuisine', 1.99, 10.00, 25, true, NOW(), NOW()),
('rest3', 'Dragon Express', 'Chinese', '789 Taste Ave, Chennai', '+91-9876543212', 'Quick Chinese food', 2.49, 12.00, 35, true, NOW(), NOW());

-- Insert sample menu items for Pizza Palace
INSERT INTO menu_items (id, restaurant_id, name, description, price, category, is_available, is_vegetarian, is_vegan, preparation_time, created_at, updated_at) VALUES
('menu1', 'rest1', 'Margherita Pizza', 'Classic tomato and mozzarella', 12.99, 'main', true, true, false, 15, NOW(), NOW()),
('menu2', 'rest1', 'Pepperoni Pizza', 'Pepperoni with mozzarella cheese', 15.99, 'main', true, false, false, 15, NOW(), NOW()),
('menu3', 'rest1', 'Caesar Salad', 'Fresh romaine with caesar dressing', 8.99, 'appetizer', true, true, false, 10, NOW(), NOW());

-- Insert sample menu items for Spice Garden
INSERT INTO menu_items (id, restaurant_id, name, description, price, category, is_available, is_vegetarian, is_vegan, preparation_time, created_at, updated_at) VALUES
('menu4', 'rest2', 'Chicken Biryani', 'Aromatic basmati rice with chicken', 18.99, 'main', true, false, false, 25, NOW(), NOW()),
('menu5', 'rest2', 'Vegetable Curry', 'Mixed vegetables in spicy curry', 14.99, 'main', true, true, true, 20, NOW(), NOW()),
('menu6', 'rest2', 'Naan Bread', 'Fresh baked Indian bread', 3.99, 'side', true, true, false, 8, NOW(), NOW());

-- Insert sample menu items for Dragon Express  
INSERT INTO menu_items (id, restaurant_id, name, description, price, category, is_available, is_vegetarian, is_vegan, preparation_time, created_at, updated_at) VALUES
('menu7', 'rest3', 'Sweet & Sour Chicken', 'Chicken in sweet and sour sauce', 16.99, 'main', true, false, false, 18, NOW(), NOW()),
('menu8', 'rest3', 'Vegetable Fried Rice', 'Wok-fried rice with vegetables', 12.99, 'main', true, true, true, 15, NOW(), NOW()),
('menu9', 'rest3', 'Spring Rolls', 'Crispy vegetable spring rolls', 6.99, 'appetizer', true, true, true, 12, NOW(), NOW());
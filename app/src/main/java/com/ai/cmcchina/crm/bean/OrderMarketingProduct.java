package com.ai.cmcchina.crm.bean;

import java.io.Serializable;

/* renamed from: com.ai.cmcchina.crm.bean.OrderMarketingProduct */
public class OrderMarketingProduct {
    private static final long serialVersionUID = 1;
    private boolean available;
    private String category;
    private String code;
    private String desc;

    /* renamed from: id */
    private String f911id;
    private String name;
    private String type;

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category2) {
        this.category = category2;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(boolean available2) {
        this.available = available2;
    }

    public String getId() {
        return this.f911id;
    }

    public void setId(String id) {
        this.f911id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public boolean equals(Object o) {
        try {
            if (this.f911id == null) {
                return false;
            }
            return this.f911id.equals(((Product) o).getId());
        } catch (Exception e) {
            return false;
        }
    }
    public static class Product implements Serializable {
        private static final long serialVersionUID = 1;
        private boolean available;
        private String category;
        private String code;
        private String desc;

        /* renamed from: id */
        private String f914id;
        private String name;
        private String type;

        public String getCategory() {
            return this.category;
        }

        public void setCategory(String category2) {
            this.category = category2;
        }

        public boolean isAvailable() {
            return this.available;
        }

        public void setAvailable(boolean available2) {
            this.available = available2;
        }

        public String getId() {
            return this.f914id;
        }

        public void setId(String id) {
            this.f914id = id;
        }

        public String getCode() {
            return this.code;
        }

        public void setCode(String code2) {
            this.code = code2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc2) {
            this.desc = desc2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }

        public boolean equals(Object o) {
            try {
                if (this.f914id == null) {
                    return false;
                }
                return this.f914id.equals(((Product) o).getId());
            } catch (Exception e) {
                return false;
            }
        }
    }

}
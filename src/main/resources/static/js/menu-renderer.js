class MenuRenderer {
    constructor(roleId, currentPageSection, menuUrl = '/config/menu-master.json', roleMapUrl = '/config/role-menu-map.json') {
        this.roleId = roleId;
        this.currentPageSection = currentPageSection; // e.g. "HOMEPAGE" or "SEGS-FACULTY"
        this.menuUrl = menuUrl;
        this.roleMapUrl = roleMapUrl;
    }

    async init() {
        try {
            const [menuRes, mapRes] = await Promise.all([
                fetch(this.menuUrl),
                fetch(this.roleMapUrl)
            ]);

            const menuData = await menuRes.json();
            const roleMap = await mapRes.json();
            const allowed = roleMap[this.roleId] || [];

            console.log("Rendering menu for roleId:", this.roleId);
            console.log("Current section:", this.currentPageSection);
            console.log("Allowed menus:", allowed);

            if (!allowed || allowed.length === 0) {
                console.warn(`No menus allowed for roleId: ${this.roleId}`);
                return;
            }

            const container = document.getElementById('menu-container');
            if (!container) {
                console.error("No #menu-container element found.");
                return;
            }

            const styles = menuData.menuSettings.styles;
            this.applyStyles(styles);

            const section = menuData.menus[this.currentPageSection];
            if (!section || !section.menuItems) {
                console.warn(`Section '${this.currentPageSection}' not found in menu data.`);
                return;
            }

            const menuBar = document.createElement('div');
            menuBar.className = 'menu-bar';

            const isFullSectionAllowed = allowed.includes(this.currentPageSection);

            section.menuItems.forEach(item => {
                const menuItemData = isFullSectionAllowed
                    ? item // show everything
                    : this.filterMenuItem(item, allowed); // filter individually

                if (menuItemData) {
                    const menuItem = this.createMenuItem(menuItemData, styles);
                    if (menuItemData.width) menuItem.style.width = `${menuItemData.width}px`;
                    if (menuItemData.height) menuItem.style.height = `${menuItemData.height}px`;
                    menuBar.appendChild(menuItem);
                }
            });

            container.appendChild(menuBar);
        } catch (err) {
            console.error("Error loading or rendering menu:", err);
        }
    }

    // Recursive filtering based on item.id
    filterMenuItem(item, allowed) {
        if (!item) return null;

        let filteredChildren = [];
        if (item.hasChildren && item.children) {
            filteredChildren = item.children
                .map(child => this.filterMenuItem(child, allowed))
                .filter(child => child !== null);
        }

        const isAllowed = allowed.includes(item.id);
        const hasAllowedChildren = filteredChildren.length > 0;

        if (isAllowed || hasAllowedChildren) {
            return {
                ...item,
                hasChildren: hasAllowedChildren,
                children: hasAllowedChildren ? filteredChildren : undefined
            };
        }

        return null;
    }

    applyStyles(styles) {
        const style = document.createElement('style');
        style.textContent = `
            .menu-bar {
                display: flex;
                flex-direction: row;
                background-color: white;
                position: fixed;
                top: ${styles.StartTop}px;
                left: ${styles.StartLeft}px;
                z-index: 1000;
            }

            .menu-item {
                position: relative;
                cursor: pointer;
                padding: ${styles.TopPadding}px ${styles.LeftPadding}px;
                border: ${styles.BorderWidth}px solid ${styles.BorderColor};
                margin-right: 0px;
                background-color: ${styles.LowBgColor};
                text-align: left;
                display: flex;
                align-items: center;
            }

            .menu-item a {
                color: ${styles.FontLowColor};
                font-family: ${styles.FontFamily};
                font-size: ${styles.FontSize}pt;
                font-weight: ${styles.FontBold ? 'bold' : 'normal'};
                text-decoration: none;
                white-space: nowrap;
                text-align: left;
            }

            .menu-item:hover {
                background-color: ${styles.HighBgColor};
            }

            .menu-item:hover > a {
                color: ${styles.FontHighColor};
            }

            .submenu {
                display: none;
                position: absolute;
                top: 100%;
                left: 0;
                background-color: ${styles.LowSubBgColor};
                border: ${styles.BorderWidth}px solid ${styles.BorderSubColor};
                min-width: 100%;
                z-index: 1001;
            }

            .menu-item:hover > .submenu {
                display: block;
            }

            .submenu .menu-item {
                display: block;
                margin: 0;
                width: 100%;
                border: none;
                border-bottom: ${styles.BorderWidth}px solid ${styles.BorderSubColor};
                background-color: ${styles.LowSubBgColor};
            }

            .submenu .menu-item a {
                color: ${styles.FontSubLowColor};
            }

            .submenu .menu-item:hover {
                background-color: ${styles.HighSubBgColor};
            }

            .submenu .menu-item:hover > a {
                color: ${styles.FontSubHighColor};
            }
        `;
        document.head.appendChild(style);
    }

    createMenuItem(item, styles) {
        const menuItem = document.createElement('div');
        menuItem.className = 'menu-item';
        if (item.id) menuItem.id = item.id;

        const link = document.createElement('a');
        link.href = item.url || '#';
        link.textContent = item.text;
        menuItem.appendChild(link);

        if (item.hasChildren && item.children) {
            const submenu = this.createSubmenu(item.children, styles);
            menuItem.appendChild(submenu);
        }

        return menuItem;
    }

    createSubmenu(children, styles) {
        const submenu = document.createElement('div');
        submenu.className = 'submenu';

        children.forEach(child => {
            const childItem = this.createMenuItem(child, styles);
            if (child.width) childItem.style.width = `${child.width}px`;
            if (child.height) childItem.style.height = `${child.height}px`;
            submenu.appendChild(childItem);
        });

        return submenu;
    }
}

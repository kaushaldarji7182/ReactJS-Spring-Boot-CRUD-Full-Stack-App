import React, { Component } from 'react';

class FooterComponent extends Component {
    render() {
        return (
            <div>
                <footer className="footer" style={footerStyle}>
                    <span>© 2025 Kaushal Darji. All rights reserved.</span>
                </footer>
            </div>
        );
    }
}

const footerStyle = {
    backgroundColor: '#f8f9fa',
    padding: '10px',
    textAlign: 'center',
    position: 'fixed',
    bottom: 0,
    width: '100%',
    fontSize: '14px',
    color: '#555'
};

export default FooterComponent;

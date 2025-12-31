export default new Promise((resolve) => {
    if (window.echarts) {
      resolve(window.echarts);
    } else {
      const interval = setInterval(() => {
        if (window.echarts) {
          clearInterval(interval);
          resolve(window.echarts);
        }
      }, 100);
    }
})
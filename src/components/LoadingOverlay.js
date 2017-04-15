import React from 'react'
import { View, ActivityIndicator } from 'react-native'

export default class LoadingOverlay extends React.Component {
  render() {
    console.log(this.props)
    if (!this.props.loading) return null
    return (
      <View style={styles.container}>
        <ActivityIndicator
          animating
          size="large"
          style={styles.indicator}
        />
      </View>
    )
  }
}

const styles = {
  container: {
    position: 'absolute',
    top: 0,
    bottom: 0,
    left: 0,
    right: 0,
    backgroundColor: 'rgba(0,0,0,0.5)',
  },
  indicator: {
    flex: 1,
    alignSelf: 'center',
  }
}
